#include <stdio.h>
#include <stdlib.h>
#include <omp.h>

int main ()
{
 int argv[0]; 
 int nthreads = atoi(argv[1]);
 #pragma omp parallel num_threads ( nthreads) 
	printf("Hello from thread %d of %d" , omp_get_thread_num() , omp_get_num_threads()) ;
 return 0;
}
