/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  te0003
 * Created: 08-Jun-2017
 */

CREATE SCHEMA IF NOT EXISTS test2;

CREATE TABLE IF NOT EXISTS test2.experiment (
    USER_ID varchar(255),
    FEMO_ID varchar(255),
    JOB_ID varchar(255),
    TIME_STAMP varchar(255),
    EXPR_RESULT MEDIUMTEXT
);

