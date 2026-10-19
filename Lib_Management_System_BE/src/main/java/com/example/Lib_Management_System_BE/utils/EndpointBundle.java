package com.example.Lib_Management_System_BE.utils;

public class EndpointBundle {

    public static final String BASEURL = "/api";
    public static final String ID = "/{id}";
    public static final String STUDENTS = "/students";
    public static final String STUDENTBYID = STUDENTS + ID;
    public static final String GETALLSTUDENTS = "/getstudents";
    public static final String UPDATESTUDENTS = "/students/{id}";

    public static final String BOOKS = "/books";
    public static final String GETALLBOOKS = "/getbooks";

    public static final String BORROW = "/borrow";
    public static final String RETURNBOOK = "/return";

    public static final String GENDERFILTER = "/students/gender/{gender}";

    public static final String LIBSUMMARY = "/summary";

    public static final String FINEPAY = "/students/{id}/fine/payment";

}