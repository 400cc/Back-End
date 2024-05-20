package Designovel.Capstone.entity;


import jakarta.persistence.*;
@Entity
public class MusinsaVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer variableId;


    private String productNum;
    private Integer malePercentage;
    private Integer femalePercentage;
    private Integer likes;
    private Integer cumulativeSales;
    private Integer ageUnder18;
    private Integer age19To23;
    private Integer age24To28;
    private Integer age29To33;
    private Integer age34To39;
    private Integer ageOver40;

}
