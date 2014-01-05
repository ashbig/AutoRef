#!/bin/sh

#cvs -d dz4@orchestra.med.harvard.edu:/cvs/hip update
git pull
ant clean
ant

tomcatctl restart dev.plasmid.med.harvard.edu