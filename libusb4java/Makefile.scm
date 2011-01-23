all:
	mkdir -p m4
	autoreconf --install --force
	
clean:
	rm -rf \
	  Debug \
	  autom4te.cache \
	  m4 \
	  aclocal.m4 \
	  config.guess \
	  config.h \
	  config.h.in \
	  config.log \
	  config.status \
	  config.sub \
	  configure \
	  depcomp \
	  install-sh \
	  INSTALL \
	  libtool \
	  ltmain.sh \
	  Makefile \
	  Makefile.in \
	  missing \
	  stamp-h1 \
	  src/main/c/Makefile.in \
	  src/main/c/.deps \
	  src/main/c/.libs \
	  src/main/c/*.lo \
	  src/main/c/*.la \
	  src/main/c/*.o \
	  src/main/c/Makefile \
	  *.tar.gz \
	  *.tar.bz2 \
          *~

