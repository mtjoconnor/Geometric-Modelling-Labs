# Makefile to compile simple svlis applications

CXX = g++
CXXFLAGS = -DSV_UNIX -pedantic -Wno-write-strings -I/usr/include/svlis
OPTFLAGS = -g -O3
LIBS = -lsvlis 
PROGRAMS = CSGmug

## This works when the includes/libraries are installed in standard places (e.g. /usr/local on ecslab)
# LIBS =  -lsvlis -lglut -lGLU -lGL -lpthread
# CXXFLAGS = -DSV_UNIX -pedantic -Wno-write-strings -I/usr/include/svlis

# This works when there is a copy of SvLis code in $(HOME) directory (but defaults to standard places)
LIBS = -L$(HOME)/SvLis/lib -lsvlis -lglut -lGLU -lGL -lpthread
CXXFLAGS = -DSV_UNIX -pedantic -Wno-write-strings -I/usr/include/svlis -I$(HOME)/SvLis/include


.SUFFIXES: .cxx .o

all: $(PROGRAMS)

.PRECIOUS: %.o
.cxx.o:
	$(CXX) -c $(OPTFLAGS) $(CXXFLAGS) -o $@ $<

.o:
	$(CXX) $(OPTFLAGS) -o $@ $< $(LIBS)
clean:
	/bin/rm -f *.o *.mod *.ppm *~
cleanest: clean
	/bin/rm -f CSGmug difference

