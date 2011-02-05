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
	  src/Makefile.in \
	  src/.deps \
	  src/.libs \
	  src/*.lo \
	  src/*.la \
	  src/*.o \
	  src/Makefile \
	  *.tar.gz \
	  *.tar.bz2 \
          *~

