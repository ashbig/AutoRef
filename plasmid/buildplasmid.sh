#!/bin/sh

#cvs -d dz4@orchestra.med.harvard.edu:/cvs/hip update
git clone git://orchestra.med.harvard.edu:/git/hip/main.git
ant clean
ant

tomcatctl restart plasmid.med.harvard.edu