package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.booking.model.ReservationSlot;
import com.restaurant.booking.booking.model.ReservationStatus;
import com.restaurant.booking.booking.service.exception.InvalidReservationStatusException;
import com.restaurant.booking.booking.service.exception.NoSlotAvailableException;
import com.restaurant.booking.booking.service.exception.ReservationNotFoundException;
import com.restaurant.booking.booking.service.repository.ReservationRepository;
import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.feign.client.UserProxy;
import com.restaurant.booking.jwt.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final ReservationSlotService slotService;
    private final UserProxy userProxy;
    private final RestaurantProxy restaurantProxy;
    private final JwtUtils jwtUtils;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationSlotService slotService, UserProxy userProxy,
                                  RestaurantProxy restaurantProxy, JwtUtils jwtUtils) {
        this.reservationRepository = reservationRepository;
        this.slotService = slotService;
        this.userProxy = userProxy;
        this.restaurantProxy = restaurantProxy;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Reservation createReservation(ReservationCreationRequest creationRequest) {

        log.info("Creating reservation for user with id {}", creationRequest.getUserId());

        Optional<ReservationSlot> possibleSlot = slotService
                .findFirstAvailableSlotsByPeople(creationRequest.getRestaurantId(), creationRequest.getNumPeople(), creationRequest.getDate());

        if(possibleSlot.isEmpty()) {
            log.warn("Did not find an available slot for configuration {}", creationRequest);
            throw new NoSlotAvailableException(creationRequest.getNumPeople(), creationRequest.getDate().toString());
        }
        Reservation reservation = new Reservation(creationRequest.getUserId(), creationRequest.getRestaurantId(),
                creationRequest.getNumPeople(), slotService.changeStatus(possibleSlot.get()));
        return save(reservation);
    }

    @Override
    public Reservation save(Reservation reservation) {

        log.info("Saving reservation with id {}", reservation.getId());
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findReservation(String reservationId) {

        log.info("Getting reservation with id {}", reservationId);
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(reservationId));
    }

    @Override
    public List<Reservation> findRestaurantReservations(String restaurantId) {

        log.info("Getting all restaurant's with id {} reservations", restaurantId);
        List<Reservation> reservations = reservationRepository.findAllByRestaurantId(restaurantId);
        reservations.forEach(this::populateAdminReservations);
        return reservations;
    }

    @Override
    public List<Reservation> findActiveUserReservations(String userId) {

        log.info("Getting all user's with id {} active reservations", userId);

        return reservationRepository.findAllByUserIdAndStatusIn(userId, List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED))
                .stream()
                .filter(reservation ->
                        reservation.getReservationSlot().getDate().isAfter(LocalDate.now()) ||
                        reservation.getReservationSlot().getDate().isEqual(LocalDate.now())
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findInactiveUserReservations(String userId) {

        log.info("Getting all user's with id {} past reservations", userId);

        return reservationRepository.findAllByUserId(userId)
                .stream()
                .filter(reservation ->
                        reservation.getReservationSlot().getDate().isBefore(LocalDate.now()) ||
                        reservation.getStatus().equals(ReservationStatus.CANCELED)
                )
                .collect(Collectors.toList());
    }

    @Override
    public Reservation changeReservationStatus(Reservation reservation, ReservationStatus status) {

        log.info("Changing reservation's with id {} status to {}", reservation.getId(), status.name());

        if(!ReservationStatus.getValidStatus(reservation.getStatus()).contains(status)) {
            log.warn("Invalid reservation status for reservation with id {}", reservation.getId());
            throw new InvalidReservationStatusException(reservation, status);
        }
        reservation.setStatus(status);

        if(status == ReservationStatus.CANCELED)
            slotService.changeStatus(reservation.getReservationSlot());

        return save(reservation);
    }

    private void populateAdminReservations(Reservation reservation){

        String userName = "";
        try{
            userName = userProxy.getUsername(jwtUtils.getAuthorizationHeader(), reservation.getUserId());
        }
        catch(Exception e){
            log.warn("User fo reservation {} not found", reservation.getId());
        }
        reservation.setUserName(userName);
    }

    private void populateUserReservations(Reservation reservation){

        String restaurantName = "";
        try{
            restaurantName = restaurantProxy.getRestaurant(jwtUtils.getAuthorizationHeader(), reservation.getRestaurantId()).getName();
        }
        catch(Exception e){
            log.warn("Restaurant for reservation {} not found", reservation.getId());
        }
        reservation.setRestaurantName(restaurantName);
    }
}
