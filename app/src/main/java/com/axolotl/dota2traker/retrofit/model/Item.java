
package com.axolotl.dota2traker.retrofit.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Item {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cost")
    @Expose
    private Integer cost;
    @SerializedName("secret_shop")
    @Expose
    private Integer secretShop;
    @SerializedName("side_shop")
    @Expose
    private Integer sideShop;
    @SerializedName("recipe")
    @Expose
    private Integer recipe;
    @SerializedName("localized_name")
    @Expose
    private String localizedName;


    public boolean isRecipe(){
        return recipe == 1;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The cost
     */
    public Integer getCost() {
        return cost;
    }

    /**
     * 
     * @param cost
     *     The cost
     */
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    /**
     * 
     * @return
     *     The secretShop
     */
    public Integer getSecretShop() {
        return secretShop;
    }

    /**
     * 
     * @param secretShop
     *     The secret_shop
     */
    public void setSecretShop(Integer secretShop) {
        this.secretShop = secretShop;
    }

    /**
     * 
     * @return
     *     The sideShop
     */
    public Integer getSideShop() {
        return sideShop;
    }

    /**
     * 
     * @param sideShop
     *     The side_shop
     */
    public void setSideShop(Integer sideShop) {
        this.sideShop = sideShop;
    }

    /**
     * 
     * @return
     *     The recipe
     */
    public Integer getRecipe() {
        return recipe;
    }

    /**
     * 
     * @param recipe
     *     The recipe
     */
    public void setRecipe(Integer recipe) {
        this.recipe = recipe;
    }

    /**
     * 
     * @return
     *     The localizedName
     */
    public String getLocalizedName() {
        return localizedName;
    }

    /**
     * 
     * @param localizedName
     *     The localized_name
     */
    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

}
