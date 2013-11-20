#!/bin/sh

cvs -d dz4@orchestra.med.harvard.edu:/cvs/hip update
ant clean
ant

tomcatctl restart plasmid.med.harvard.edu