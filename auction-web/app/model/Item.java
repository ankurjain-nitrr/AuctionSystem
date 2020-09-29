package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {

    private String code;
    private String name;
    private String category;
    private int actualPrice;

}
