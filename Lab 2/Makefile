# Makefile to compile simple svlis applications

CXX = g++
OPTFLAGS = -g -O3
PROGRAMS = CSGmug

# Path to an SvLis checkout (override on command line if needed):
#   make CSGmug SVLIS_DIR=/path/to/SvLis
SVLIS_DIR ?= $(CURDIR)/SvLis
UNAME_S := $(shell uname -s)

# Build local SvLis library automatically when the checkout exists.
# Set SVLIS_BUILD=0 to skip this step.
SVLIS_BUILD ?= 1

CXXFLAGS = -DSV_UNIX -pedantic -Wno-write-strings -DGL_SILENCE_DEPRECATION -I/usr/include/svlis -I"$(SVLIS_DIR)/include"
LIBS = -L"$(SVLIS_DIR)/lib" -lsvlis -lpthread

ifeq ($(UNAME_S),Darwin)
LIBS += -framework GLUT -framework OpenGL
else
LIBS += -lglut -lGLU -lGL
endif

all: $(PROGRAMS)

CSGmug.o: CSGmug.cxx
ifeq ($(SVLIS_BUILD),1)
	@if [ -f "$(SVLIS_DIR)/Makefile" ]; then \
		$(MAKE) -C "$(SVLIS_DIR)" library; \
	else \
		echo "Info: $(SVLIS_DIR)/Makefile not found; skipping local SvLis build."; \
	fi
endif
	$(CXX) -c $(OPTFLAGS) $(CXXFLAGS) -o $@ $<

CSGmug: CSGmug.o
	$(CXX) $(OPTFLAGS) -o $@ $< $(LIBS)
clean:
	/bin/rm -f *.o *.mod *.ppm *~
cleanest: clean
	/bin/rm -f CSGmug difference
