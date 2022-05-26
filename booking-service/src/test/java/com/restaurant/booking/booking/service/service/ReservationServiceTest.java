package com.restaurant.booking.booking.service.service;

import com.restaurant.booking.booking.model.Reservation;
import com.restaurant.booking.booking.model.ReservationCreationRequest;
import com.restaurant.booking.booking.model.ReservationSlot;
import com.restaurant.booking.booking.model.ReservationStatus;
import com.restaurant.booking.booking.service.exception.InvalidReservationStatusException;
import com.restaurant.booking.booking.service.exception.NoSlotAvailableException;
import com.restaurant.booking.booking.service.exception.ReservationNotFoundException;
import com.restaurant.booking.booking.service.repository.ReservationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationSlotServiceImpl slotService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation(){
        ReservationCreationRequest request = new ReservationCreationRequest(
                "id", "id", 2, LocalDate.now(), LocalTime.now());
        Optional<ReservationSlot> slotOptional = Optional.of(ReservationSlot.builder().build());
        Reservation reservation = Reservation.builder()
                .userId("id")
                .restaurantId("id")
                .numPeople(2)
                .reservationSlot(slotOptional.get())
                .status(ReservationStatus.PENDING)
                .build();

        Mockito.when(slotService.findFirstAvailableSlotsByPeople("id", 2, LocalDate.now())).thenReturn(slotOptional);
        Mockito.when(slotService.changeStatus(slotOptional.get())).thenReturn(slotOptional.get());
        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation obtainedReservation = reservationService.createReservation(request);

        Mockito.verify(slotService).findFirstAvailableSlotsByPeople("id", 2, LocalDate.now());
        Mockito.verify(slotService).changeStatus(slotOptional.get());
        Mockito.verify(reservationRepository).save(reservation);
        Assertions.assertEquals(reservation, obtainedReservation);
    }

    @Test
    void createReservation_noSlot(){
        ReservationCreationRequest request = new ReservationCreationRequest(
                "id", "id", 2, LocalDate.now(), LocalTime.now());
        Optional<ReservationSlot> slotOptional = Optional.empty();

        Mockito.when(slotService.findFirstAvailableSlotsByPeople("id", 2, LocalDate.now())).thenReturn(slotOptional);

        NoSlotAvailableException exception = Assertions.assertThrows(
                NoSlotAvailableException.class, () -> reservationService.createReservation(request));
        Assertions.assertEquals("No slot available for 2 people the " + LocalDate.now(), exception.getMessage());

        Mockito.verify(slotService).findFirstAvailableSlotsByPeople("id", 2, LocalDate.now());
    }

    @Test
    void findReservation(){
        Reservation reservation = Reservation.builder().build();

        Mockito.when(reservationRepository.findById("id")).thenReturn(Optional.of(reservation));

        Reservation obtainedReservation = reservationService.findReservation("id");

        Mockito.verify(reservationRepository).findById("id");
        Assertions.assertEquals(reservation, obtainedReservation);
    }

    @Test
    void findReservation_notFound(){
        Mockito.when(reservationRepository.findById("id")).thenReturn(Optional.empty());

        ReservationNotFoundException exception = Assertions.assertThrows(
                ReservationNotFoundException.class, () -> reservationService.findReservation("id"));
        Assertions.assertEquals("Reservation with id id not found", exception.getMessage());

        Mockito.verify(reservationRepository).findById("id");
    }

    @Test
    void findRestaurantReservations(){
        List<Reservation> reservations = List.of(Reservation.builder().build());

        Mockito.when(reservationRepository.findAllByRestaurantId("id")).thenReturn(reservations);

        List<Reservation> obtainedReservations = reservationService.findRestaurantReservations("id");

        Mockito.verify(reservationRepository).findAllByRestaurantId("id");
        Assertions.assertEquals(reservations, obtainedReservations);
    }

    @Test
    void findActiveUserReservations(){
        List<Reservation> reservations = List.of(
                Reservation.builder().reservationSlot(
                        ReservationSlot.builder().date(LocalDate.now()).build()).build(),
                Reservation.builder().reservationSlot(
                        ReservationSlot.builder().date(LocalDate.now().minus(Period.ofDays(2))).build()).build()
        );

        Mockito.when(reservationRepository.findAllByUserIdAndStatusIn("id", List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED)))
                .thenReturn(reservations);

        List<Reservation> obtainedReservations = reservationService.findActiveUserReservations("id");

        Mockito.verify(reservationRepository).findAllByUserIdAndStatusIn("id", List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED));
        Assertions.assertEquals(List.of(reservations.get(0)), obtainedReservations);
    }

    @Test
    void findInactiveUserReservations(){
        List<Reservation> reservations = List.of(
                Reservation.builder().reservationSlot(
                        ReservationSlot.builder().date(LocalDate.now()).build())
                        .status(ReservationStatus.CANCELED).build(),
                Reservation.builder().reservationSlot(
                        ReservationSlot.builder().date(LocalDate.now().minus(Period.ofDays(2))).build())
                        .status(ReservationStatus.CONFIRMED).build()
        );

        Mockito.when(reservationRepository.findAllByUserId("id")).thenReturn(reservations);

        List<Reservation> obtainedReservations = reservationService.findInactiveUserReservations("id");

        Mockito.verify(reservationRepository).findAllByUserId("id");
        Assertions.assertEquals(reservations, obtainedReservations);
    }

    @Test
    void changeReservationStatus(){
        Reservation reservation = Reservation.builder()
                .status(ReservationStatus.PENDING)
                .reservationSlot(ReservationSlot.builder().build()).build();
        reservation.setStatus(ReservationStatus.CANCELED);

        Mockito.when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation obtainedReservation = reservationService.changeReservationStatus(reservation, ReservationStatus.CANCELED);

        Mockito.verify(slotService).changeStatus(reservation.getReservationSlot());
        Mockito.verify(reservationRepository).save(reservation);
        Assertions.assertEquals(reservation, obtainedReservation);
    }

    @Test
    void changeReservationStatus_invalidStatus(){
        Reservation reservation = Reservation.builder()
                .id("id")
                .status(ReservationStatus.CANCELED)
                .reservationSlot(ReservationSlot.builder().build()).build();

        InvalidReservationStatusException exception = Assertions.assertThrows(
                InvalidReservationStatusException.class, () -> reservationService.changeReservationStatus(reservation, ReservationStatus.CONFIRMED));
        Assertions.assertEquals("Invalid status CONFIRMED for reservation with id id, valid statuses are "
                + ReservationStatus.getValidStatus(ReservationStatus.CANCELED), exception.getMessage());
    }
}
