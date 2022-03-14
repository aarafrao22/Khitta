package com.example.khitta.Models;

public class FavoritesModel {
    String favImage,favPrice,favRooms,favBedRooms;

    public FavoritesModel() {
    }

    public FavoritesModel(String favImage, String favPrice, String favRooms, String favBedRooms) {
        this.favImage = favImage;
        this.favPrice = favPrice;
        this.favRooms = favRooms;
        this.favBedRooms = favBedRooms;
    }

    public String getFavImage() {
        return favImage;
    }

    public void setFavImage(String favImage) {
        this.favImage = favImage;
    }

    public String getFavPrice() {
        return favPrice;
    }

    public void setFavPrice(String favPrice) {
        this.favPrice = favPrice;
    }

    public String getFavRooms() {
        return favRooms;
    }

    public void setFavRooms(String favRooms) {
        this.favRooms = favRooms;
    }

    public String getFavBedRooms() {
        return favBedRooms;
    }

    public void setFavBedRooms(String favBedRooms) {
        this.favBedRooms = favBedRooms;
    }
}
