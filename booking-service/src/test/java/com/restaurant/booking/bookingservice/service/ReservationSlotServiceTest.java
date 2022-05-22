package com.restaurant.booking.bookingservice.service;

import com.restaurant.booking.booking.model.ReservSlotsCreationRequest;
import com.restaurant.booking.booking.model.ReservationSlot;
import com.restaurant.booking.booking.model.ReservationSlotStatus;
import com.restaurant.booking.bookingservice.exception.ReservationSlotNotFoundException;
import com.restaurant.booking.bookingservice.repository.ReservationSlotRepository;
import com.restaurant.booking.feign.client.TableProxy;
import com.restaurant.booking.table.model.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationSlotServiceTest {

    @Mock
    private ReservationSlotRepository slotRepository;

    @Mock
    private TableProxy tableProxy;

    @InjectMocks
    private ReservationSlotServiceImpl slotService;

    @Test
    void createRestaurantSlots(){
        ReservSlotsCreationRequest slotsCreationRequest = ReservSlotsCreationRequest.builder()
                .restaurantId("id")
                .reservationHours(List.of(LocalTime.now()))
                .build();
        List<Table> tables = List.of(new Table());

        Mockito.when(tableProxy.getRestaurantTables(slotsCreationRequest.getRestaurantId())).thenReturn(tables);

        slotService.createRestaurantSlots(slotsCreationRequest);

        Mockito.verify(tableProxy).getRestaurantTables(slotsCreationRequest.getRestaurantId());
        Mockito.verify(slotRepository, Mockito.times(31)).save(Mockito.any(ReservationSlot.class));
    }

    @Test
    void createRestaurantSlots_withTable(){
        ReservSlotsCreationRequest slotsCreationRequest = ReservSlotsCreationRequest.builder()
                .restaurantId("id")
                .reservationHours(List.of(LocalTime.now()))
                .table(new Table())
                .build();

        slotService.createRestaurantSlots(slotsCreationRequest);

        Mockito.verify(slotRepository, Mockito.times(31)).save(Mockito.any());
    }
    
    @Test
    void findSlot(){
        ReservationSlot reservationSlot = ReservationSlot.builder().id("id").build();

        Mockito.when(slotRepository.findById("id")).thenReturn(Optional.of(reservationSlot));

        ReservationSlot slot = slotService.findSlot("id");

        Mockito.verify(slotRepository).findById("id");
        Assertions.assertEquals(reservationSlot, slot);
    }

    @Test
    void findSlot_notFound(){
        Mockito.when(slotRepository.findById("id")).thenReturn(Optional.empty());

        ReservationSlotNotFoundException exception = Assertions.assertThrows(
                ReservationSlotNotFoundException.class, () -> slotService.findSlot("id"));
        Assertions.assertEquals("Reservation slot with id id not found", exception.getMessage());

        Mockito.verify(slotRepository).findById("id");
    }

    @Test
    void findAllAvailableSlotsByPeople(){
        List<ReservationSlot> slots = List.of(
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(1).maxPeople(3).build())
                        .time(LocalTime.of(10,00))
                        .build(),
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(1).maxPeople(2).build())
                        .time(LocalTime.of(20,00))
                        .build());

        Mockito.when(slotRepository.findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE))
                .thenReturn(slots);

        List<LocalTime> obtainedTimeSlots = slotService.findAllAvailableSlotsByPeople("id", 3, LocalDate.now());

        Mockito.verify(slotRepository).findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE);
        Assertions.assertEquals(List.of(slots.get(0).getTime()), obtainedTimeSlots);
    }

    @Test
    void findFirstAvailableSlotsByPeople_sameMin(){
        List<ReservationSlot> slots = List.of(
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(1).maxPeople(3).build())
                        .time(LocalTime.of(10,00))
                        .build(),
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(1).maxPeople(2).build())
                        .time(LocalTime.of(20,00))
                        .build());

        Mockito.when(slotRepository.findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE))
                .thenReturn(slots);

        Optional<ReservationSlot> obtainedSlot = slotService.findFirstAvailableSlotsByPeople("id", 2, LocalDate.now());

        Mockito.verify(slotRepository).findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE);
        Assertions.assertEquals(slots.get(1), obtainedSlot.get());
    }

    @Test
    void findFirstAvailableSlotsByPeople_diffMin(){
        List<ReservationSlot> slots = List.of(
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(2).maxPeople(3).build())
                        .time(LocalTime.of(10,00))
                        .build(),
                ReservationSlot.builder()
                        .table(Table.builder().minPeople(1).maxPeople(2).build())
                        .time(LocalTime.of(20,00))
                        .build());

        Mockito.when(slotRepository.findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE))
                .thenReturn(slots);

        Optional<ReservationSlot> obtainedSlot = slotService.findFirstAvailableSlotsByPeople("id", 2, LocalDate.now());

        Mockito.verify(slotRepository).findAllByRestaurantIdAndDateAndStatus("id", LocalDate.now(), ReservationSlotStatus.FREE);
        Assertions.assertEquals(slots.get(1), obtainedSlot.get());
    }

    @Test
    void changeStatus_bothSidesSlots(){
        Table table = Table.builder().minPeople(2).maxPeople(3).build();
        ReservationSlot slot = ReservationSlot.builder()
                .table(table)
                .date(LocalDate.now())
                .time(LocalTime.of(9,30))
                .status(ReservationSlotStatus.FREE)
                .build();
        List<ReservationSlot> tableSlots = List.of(
                ReservationSlot.builder()
                        .table(table)
                        .date(LocalDate.now())
                        .time(LocalTime.of(9,00))
                        .status(ReservationSlotStatus.FREE)
                        .build(),
                slot,
                ReservationSlot.builder()
                        .table(table)
                        .date(LocalDate.now())
                        .time(LocalTime.of(10,00))
                        .status(ReservationSlotStatus.FREE)
                        .build());
        ReservationSlot slotBooked = slot;
        slotBooked.setStatus(ReservationSlotStatus.BOOKED);

        Mockito.when(slotRepository.findAllByTableAndDate(slot.getTable(), slot.getDate())).thenReturn(tableSlots);
        Mockito.when(slotRepository.save(slot)).thenReturn(slotBooked);
        Mockito.when(slotRepository.save(tableSlots.get(0))).thenReturn(null);
        Mockito.when(slotRepository.save(tableSlots.get(2))).thenReturn(null);

        ReservationSlot obtainedSlot = slotService.changeStatus(slot);

        Mockito.verify(slotRepository).findAllByTableAndDate(slot.getTable(), slot.getDate());
        Mockito.verify(slotRepository, Mockito.times(3)).save(Mockito.any(ReservationSlot.class));
        Assertions.assertEquals(slotBooked, obtainedSlot);
    }

    @Test
    void changeStatus_oneSideSlot(){
        Table table = Table.builder().minPeople(2).maxPeople(3).build();
        ReservationSlot slot = ReservationSlot.builder()
                .table(table)
                .date(LocalDate.now())
                .time(LocalTime.of(9,30))
                .status(ReservationSlotStatus.FREE)
                .build();
        List<ReservationSlot> tableSlots = List.of(
                slot,
                ReservationSlot.builder()
                        .table(table)
                        .date(LocalDate.now())
                        .time(LocalTime.of(10,00))
                        .status(ReservationSlotStatus.FREE)
                        .build());
        ReservationSlot slotBooked = slot;
        slotBooked.setStatus(ReservationSlotStatus.BOOKED);

        Mockito.when(slotRepository.findAllByTableAndDate(slot.getTable(), slot.getDate())).thenReturn(tableSlots);
        Mockito.when(slotRepository.save(slot)).thenReturn(slotBooked);
        Mockito.when(slotRepository.save(tableSlots.get(1))).thenReturn(null);

        ReservationSlot obtainedSlot = slotService.changeStatus(slot);

        Mockito.verify(slotRepository).findAllByTableAndDate(slot.getTable(), slot.getDate());
        Mockito.verify(slotRepository, Mockito.times(2)).save(Mockito.any(ReservationSlot.class));
        Assertions.assertEquals(slotBooked, obtainedSlot);
    }
}
