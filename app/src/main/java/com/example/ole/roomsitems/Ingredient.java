package com.example.ole.roomsitems;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "ingredient_name")
    private String name;

    @ColumnInfo(name = "ingredient_amount")
    private float amount;

}