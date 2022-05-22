package com.restaurant.booking.bookingservice.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.model.ReservationSlot;
import com.restaurant.booking.booking.model.ReservationSlotStatus;
import com.restaurant.booking.bookingservice.exception.ReservationSlotNotFoundException;
import com.restaurant.booking.bookingservice.repository.ReservationSlotRepository;
import com.restaurant.booking.feign.client.TableProxy;
import com.restaurant.booking.table.model.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationSlotServiceImpl implements ReservationSlotService {

    private final ReservationSlotRepository slotRepository;
    private final TableProxy tableProxy;

    @Autowired
    public ReservationSlotServiceImpl(ReservationSlotRepository slotRepository, TableProxy tableProxy) {
        this.slotRepository = slotRepository;
        this.tableProxy = tableProxy;
    }

    @Override
    public ReservationSlot save(ReservationSlot slot) {
        return slotRepository.save(slot);
    }

    @Override
    public void createRestaurantSlots(ReservSlotsCreationRequest slotsCreationRequest) {

        log.info("Creating all reservation slots for restaurant with id {}", slotsCreationRequest.getRestaurantId());
        // obtengo las mesas del restaurante
        List<Table> tables;
        if(slotsCreationRequest.getTable() != null)
            tables = List.of(slotsCreationRequest.getTable());
        else
            tables = tableProxy.getRestaurantTables(slotsCreationRequest.getRestaurantId());

        // para un mes, a partir del día actual y cada mesa creo un reservation slot, según las reservation hours del restaurante
        LocalDate date = LocalDate.now();
        LocalDate endDate = date.plusDays(31);

        while (date.isBefore(endDate)){
            for(Table table: tables) {
                for (LocalTime time : slotsCreationRequest.getReservationHours()) {
                    save(ReservationSlot.builder()
                            .date(date)
                            .time(time)
                            .status(ReservationSlotStatus.FREE)
                            .restaurantId(slotsCreationRequest.getRestaurantId())
                            .table(table)
                            .build());
                }
            }
            date = date.plusDays(1);
        }
    }

    @Override
    public ReservationSlot findSlot(String slotId) {

        log.info("Getting reservation slot with id {}", slotId);
        return slotRepository.findById(slotId).orElseThrow(() -> new ReservationSlotNotFoundException(slotId));
    }

    @Override
    public List<LocalTime> findAllAvailableSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date) {
        log.info("Finding all available slot for {} people in restaurant {}", numPeople, restaurantId);
        return slotRepository.findAllByRestaurantIdAndDateAndStatus(restaurantId, date, ReservationSlotStatus.FREE)
                .stream()
                .filter(slot -> slot.getTable().getMinPeople() <= numPeople && slot.getTable().getMaxPeople() >= numPeople)
                .map(ReservationSlot::getTime)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservationSlot> findFirstAvailableSlotsByPeople(String restaurantId, Integer numPeople, LocalDate date) {
        return slotRepository.findAllByRestaurantIdAndDateAndStatus(restaurantId, date, ReservationSlotStatus.FREE)
                .stream()
                .filter(slot -> slot.getTable().getMinPeople() <= numPeople && slot.getTable().getMaxPeople() >= numPeople)
                .min((s1, s2) -> {
                    if (Objects.equals(s1.getTable().getMinPeople(), s2.getTable().getMinPeople())) {
                        return Integer.compare(s1.getTable().getMaxPeople(), s2.getTable().getMaxPeople());
                    }
                    return Integer.compare(s1.getTable().getMinPeople(), s2.getTable().getMinPeople());
                });
    }

    @Override
    public ReservationSlot changeStatus(ReservationSlot slot) {

        log.info("Changing the status of the reservation slot with id {}", slot.getId());

        List<ReservationSlot> tableSlots = slotRepository.findAllByTableAndDate(slot.getTable(), slot.getDate());

        Optional<ReservationSlot> prevSlotOpt = tableSlots
                .stream()
                .filter(s -> s.getTime().isBefore(slot.getTime()))
                .max(Comparator.comparing(ReservationSlot::getTime));
        persistAdjacentSlot(slot.getStatus(), prevSlotOpt);

        Optional<ReservationSlot> nextSlotOpt = tableSlots
                .stream()
                .filter(s -> s.getTime().isAfter(slot.getTime()))
                .min(Comparator.comparing(ReservationSlot::getTime));
        persistAdjacentSlot(slot.getStatus(), nextSlotOpt);

        slot.setStatus(slot.getStatus().getNextStatus());
        return save(slot);
    }

    private void persistAdjacentSlot(ReservationSlotStatus parentSlotStatus, Optional<ReservationSlot> currentSlotOpt) {
        if(currentSlotOpt.isPresent()){
            ReservationSlot currentSlot = currentSlotOpt.get();
            currentSlot.setStatus(currentSlot.getStatus().getNextStatusByParent(parentSlotStatus));
            save(currentSlot);
        }
    }
}
