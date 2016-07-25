import React from 'react';


function testFun() {
    console.log('Test fun called');
}

const Bundle = {
    React,
    testFun,
};

module.exports = Bundle;

console.log('Main!');
