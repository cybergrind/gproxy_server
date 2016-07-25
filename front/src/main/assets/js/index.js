import React from 'react';


function testFun() {
    console.log('Test fun called');
}

// global.testFun = testFun;

global.Bundle = {
    React,
    testFun,
};

console.log('Main!');
