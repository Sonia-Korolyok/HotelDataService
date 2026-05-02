package hotel.generator;

import hotel.models.*;

import java.util.Map;

public record InitialData(
        Map<String, RoomType> roomTypes,
        Map<Integer, Room> rooms,
        Map<Integer, Guest> guests,
        Map<Integer, Booking> bookings
) {}

