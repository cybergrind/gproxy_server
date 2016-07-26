import React from 'react';
import ReactDOM from 'react-dom';
// TODO: separate prod / test imports
import '../../../../target/scala-2.11/front-fastopt.js';


function testFun() {
    console.log('Test fun called!');
}

function log(what) {
    console.log(what);
}


const Bundle = {
    React,
    ReactDOM,
    testFun,
    log,
};

global.React = React;
global.ReactDOM = ReactDOM;
global.Bundle = Bundle;

export default Bundle;

console.log('Main!');

/* global kpi */
/* eslint new-cap:"off" */
document.addEventListener('DOMContentLoaded', () => {
    kpi.gproxy.front.MainApp().main();
});
