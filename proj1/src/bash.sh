#!/bin/bash

function p() {
        java -cp build main.Peer 1 $1 230.0.0.1 1889 230.0.0.2 1888 230.0.0.3 1887
}

function c() {
        java -cp build main.Client $1 $2 $3 $4
}