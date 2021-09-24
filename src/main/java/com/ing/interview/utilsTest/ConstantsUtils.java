package com.ing.interview.utilsTest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsUtils {

    /*Parts of paths for controller*/
    public static final String PATH_SEPARATOR="/";
    public static final String CARS="cars";
    public static final String ORDERS="order";
    public static final String API = "api";
    public static final String API_VERSION = "v1";
    public static final String CREATE_CAR = "car";
    public static final String CREATE_CAR_EXTENDED = "carExtended";
    public static final String CREATE_ORDER = "order";
    public static final String ID_CAR="{idCar}";
    public static final String FIND_CAR = "findCar";

    /*Definitions stock service*/
    public static final String ING_STOCK_SERVICE = "stockService";
    public static final String ING_STOCK_SERVICE_PATH_TIMEOUT = "Timeout";

    /*Definitions color picker service*/
    public static final String ING_COLOR_PICKER_SERVICE = "colorPickerService";
    public static final String ING_COLOR_PICKER_SERVICE_PATH_TIMEOUT = "Timeout";

    /*Definitions insurance service*/
    public static final String ING_INSURANCE_SERVICE = "insuranceService";
    public static final String ING_INSURANCE_SERVICE_PATH_TIMEOUT = "Timeout";

    /*Definitions order conector service*/
    public static final String ING_ORDER_CONECTOR_SERVICE = "orderConectorService";
    public static final String ING_ORDER_CONECTOR_SERVICE_PATH_TIMEOUT = "Timeout";
}
