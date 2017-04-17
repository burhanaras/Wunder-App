package com.burhan.wunderapp.domain;

import com.burhan.wunderapp.domain.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class DistanceCalculatorUnitTest {

    private DistanceCalculator distanceCalculator;
    private LatLng from = new LatLng(53.5618858, 9.9614671);

    @Before
    public void setUp() {
        distanceCalculator = new DistanceCalculator(from);
    }

    @Test
    public void distance_distance() throws Exception {
        LatLng from = new LatLng(0,0);
        LatLng to = new LatLng(3,4);

        distanceCalculator = new DistanceCalculator(from);
        double expected = 5;
        double actual = distanceCalculator.distance(to);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void distance_near() throws Exception {
        LatLng from = new LatLng(53.5770154, 10.0192738);
        LatLng to = new LatLng(53.5752572, 10.0212049);

        distanceCalculator = new DistanceCalculator(from);
        double expected = DistanceCalculator.NEAR;
        double actual = distanceCalculator.rangeTo(to);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void distance_medium() throws Exception {
        LatLng from = new LatLng(53, 10);
        LatLng to = new LatLng(53.006388, 10.07838);

        distanceCalculator = new DistanceCalculator(from);
        double expected = DistanceCalculator.MEDIUM;
        double actual = distanceCalculator.rangeTo(to);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void distance_far() throws Exception {
        LatLng from = new LatLng(53, 10);
        LatLng to = new LatLng(53.06388, 10.07838);

        distanceCalculator = new DistanceCalculator(from);
        double expected = DistanceCalculator.FAR;
        double actual = distanceCalculator.rangeTo(to);
        Assert.assertEquals(expected, actual);
    }


    @Test
    public void distance_real() throws Exception {
        LatLng from = new LatLng(53.5598157,10.0107712);
        LatLng to = new LatLng(53.5588566,10.0083518);

        distanceCalculator = new DistanceCalculator(from);
        double expected = 192;
        double actual = distanceCalculator.distance(from.latitude, to.latitude, from.longitude, to.longitude, 0, 0);
        Assert.assertEquals(expected, actual,1);
    }


}