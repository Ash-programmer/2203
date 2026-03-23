package com.example.domain;

public class Room {

    private int roomNumber;
    private RoomType type;

    public Room(int roomNumber, RoomType type) {
        this.roomNumber = roomNumber;
        this.type = type;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public boolean isBattleRoom() {
        return type == RoomType.BATTLE;
    }

    public boolean isInnRoom() {
        return type == RoomType.INN;
    }

    public static Room randomRoom(int number) {

        if (Math.random() < 0.6) {
            return new Room(number, RoomType.BATTLE);
        } else {
            return new Room(number, RoomType.INN);
        }
    }
}