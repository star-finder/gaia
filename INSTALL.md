`Gaia` requires either (`Latte` + `Omega`) or `barvinok` to count models of linear constraints. Here are the instructions to install all the tools to `/home/qsphan/programs/bin`.
## 1. Install barvinok
Get barvinok
```
$ cd /home/qsphan/programs/src
$ wget http://barvinok.gforge.inria.fr/barvinok-0.41.tar.gz
$ tar xf barvinok-0.41.tar.gz
```
Instructions to install the library can be found in `README`. The following instructions are for the impatient.
barvinok requires GMP and NTL
### Install GMP
```
$ cd /home/qsphan/programs/src
$ wget https://gmplib.org/download/gmp/gmp-6.1.0.tar.xz
$ tar xf gmp-6.1.0.tar.xz
$ cd gmp-6.1.0
$ ./configure PREFIX=/home/qsphan/programs/bin
```
If `m4` is missing, install it `sudo apt-get install m4`. Then
```
$ make
$ make check
$ make install
```
### Install NTL
```
$ cd /home/qsphan/programs/src
$ wget https://shoup.net/ntl/ntl-11.4.1.tar.gz
$ tar xf ntl-11.4.1.tar.gz
$ cd ntl-11.4.1/src
$ ./configure NTL_GMP_LIP=on NTL_STD_CXX11=on PREFIX=/home/qsphan/programs/bin GMP_PREFIX=/home/qsphan/programs/bin
```
### Install barvinok
```
$ cd /home/qsphan/programs/src/barvinok-0.41
$ ./configure --prefix=/home/qsphan/programs/bin --with-gmp-prefix=/home/qsphan/programs/bin --with-ntl-prefix=/home/qsphan/programs/bin
$ make
$ make check
$ make install
```
