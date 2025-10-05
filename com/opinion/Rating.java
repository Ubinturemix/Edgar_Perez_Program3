package com.opinion;

/*
 Author:     Edgar Perez
 Assignment: Program 1 - Reviews, Reduced Boilerplate
 Class:      CSC 2040
 */

import java.io.Serializable;


//Represents a discrete rating with an associated star count.
//Allowed ratings: ONE, TWO, THREE.
public enum Rating {
    //One-star rating.
    ONE(1),
    //Two-star rating.
    TWO(2),
    //Three-star rating.
    THREE(3);

    //star count for the rating
    private final int stars;

    //Construct a Rating with the given star count
     //@param stars number of stars (positive)
    Rating(int stars) {
        this.stars = stars;
    }

    /*
     Returns how many stars this rating represents.
     @return star count (1, 2, or 3)
     */
    public int getStars() {
        return stars;
    }

}
