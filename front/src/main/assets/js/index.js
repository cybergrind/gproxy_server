import React from 'react';
import ReactDOM from 'react-dom';


function testFun() {
    console.log('Test fun called!');
}

function log(what) {
    console.log(what);
}

global.React = React
global.ReactDOM = ReactDOM

const Bundle = {
    React,
    ReactDOM,
    testFun,
    log,
};

module.exports = Bundle;

console.log('Main!');
