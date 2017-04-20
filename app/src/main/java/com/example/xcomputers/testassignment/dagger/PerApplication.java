package com.example.xcomputers.testassignment.dagger;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Georgi on 4/20/2017.
 */
@Qualifier
@Retention(RUNTIME)
@interface PerApplication {
}
