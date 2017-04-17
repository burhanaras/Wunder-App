package com.burhan.wunderapp.data;

import com.burhan.wunderapp.data.remote.WunderPlacemarksRestService;
import com.burhan.wunderapp.data.remote.model.Placemark;
import com.burhan.wunderapp.data.remote.model.PlacemarksList;
import com.burhan.wunderapp.data.repository.WunderRepository;
import com.burhan.wunderapp.data.repository.WunderRepositoryImpl;
import com.burhan.wunderapp.domain.DistanceCalculator;
import com.google.android.gms.maps.model.LatLng;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class WunderRepositoryUnitTest {

    private static final String PLACEMARK1_NAME = "Placemark1";
    private static final String PLACEMARK2_NAME = "Placemark2";

    @Mock
    WunderPlacemarksRestService wunderPlacemarksRestService;

    private WunderRepository wunderRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        wunderRepository = new WunderRepositoryImpl(wunderPlacemarksRestService);
    }

    @Test
    public void search_placemarks_200OK_api_calls_should_return_results(){
        //Given
        when(wunderPlacemarksRestService.searchPlacemarks()).thenReturn(Observable.just(placeMarkList()));

        //When
        TestSubscriber<List<Placemark>> subscriber = new TestSubscriber<>();
        wunderRepository.searchPlacemarks().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<List<Placemark>> onNextEvents = subscriber.getOnNextEvents();
        List<Placemark> placemarks = onNextEvents.get(0);
        Assert.assertEquals(PLACEMARK1_NAME, placemarks.get(0).getName());
        Assert.assertEquals(PLACEMARK2_NAME, placemarks.get(1).getName());
        verify(wunderPlacemarksRestService).searchPlacemarks();
    }
    @Test
    public void search_placemarks_200OK_api_calls_should_return_only_good_interior_and_exterior_results(){
        //Given
        PlacemarksList list =placeMarkList();
        list.getItems().add(dummyPlaceMarkUnacceptable());
        when(wunderPlacemarksRestService.searchPlacemarks()).thenReturn(Observable.just(placeMarkList()));

        //When
        TestSubscriber<List<Placemark>> subscriber = new TestSubscriber<>();
        wunderRepository.searchPlacemarks().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        List<List<Placemark>> onNextEvents = subscriber.getOnNextEvents();
        List<Placemark> placemarks = onNextEvents.get(0);
        Assert.assertEquals(PLACEMARK1_NAME, placemarks.get(0).getName());
        Assert.assertEquals(PLACEMARK2_NAME, placemarks.get(1).getName());
        int expected = list.getItems().size() -1;
        int actual = placemarks.size();
        Assert.assertEquals(expected,actual);
        verify(wunderPlacemarksRestService).searchPlacemarks();
    }

    @Test
    public void searchPlacemarks_IOExceptionThenSuccess_SearchPlacemarksRetried() {
        //Given
        when(wunderPlacemarksRestService.searchPlacemarks())
                .thenReturn(getIOExceptionError(), Observable.just(placeMarkList()));

        //When
        TestSubscriber<List<Placemark>> subscriber = new TestSubscriber<>();
        wunderRepository.searchPlacemarks().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertNoErrors();

        verify(wunderPlacemarksRestService, times(2)).searchPlacemarks();
    }


    private Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }

    @Test
    public void searchUsers_OtherHttpError_SearchTerminatedWithError() {
        //Given
        when(wunderPlacemarksRestService.searchPlacemarks()).thenReturn(get403ForbiddenError());

        //When
        TestSubscriber<List<Placemark>> subscriber = new TestSubscriber<>();
        wunderRepository.searchPlacemarks().subscribe(subscriber);

        //Then
        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);

        verify(wunderPlacemarksRestService).searchPlacemarks();

    }
    private Observable<PlacemarksList> get403ForbiddenError() {
        return Observable.error(new HttpException(
                Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Forbidden"))));

    }


    private PlacemarksList placeMarkList(){
        PlacemarksList placemarksList = new PlacemarksList();
        List<Placemark> list=new ArrayList<>();
        list.add(dummyPlaceMark1());
        list.add(dummyPlaceMark2());
        placemarksList.setItems(list);
        return placemarksList;
    }

    private Placemark dummyPlaceMark1(){
        Placemark placemark= new Placemark();
        placemark.setCoordinates(new double[]{20,30});
        placemark.setFuel(30);
        placemark.setVin("Vin");
        placemark.setName(PLACEMARK1_NAME);
        placemark.setAddress("address 1");
        placemark.setInterior("GOOD");
        placemark.setExterior("GOOD");
        return placemark;
    }
    private Placemark dummyPlaceMark2(){
        Placemark placemark= new Placemark();
        placemark.setCoordinates(new double[]{30,40});
        placemark.setFuel(60);
        placemark.setVin("Vin");
        placemark.setName(PLACEMARK2_NAME);
        placemark.setAddress("address 1");
        placemark.setInterior("GOOD");
        placemark.setExterior("GOOD");
        return placemark;
    }
    private Placemark dummyPlaceMarkUnacceptable(){
        Placemark placemark= new Placemark();
        placemark.setCoordinates(new double[]{30,40});
        placemark.setFuel(60);
        placemark.setVin("Vin");
        placemark.setName(PLACEMARK2_NAME);
        placemark.setAddress("address 1");
        placemark.setInterior("UNACCEPTABLE");
        placemark.setExterior("GOOD");
        return placemark;
    }


}