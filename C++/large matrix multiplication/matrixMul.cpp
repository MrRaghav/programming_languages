/**
 * @file   matrixMul.cpp
 * @author Raghvendra Pratap Singh, M.Sc. in Computing, Dublin City University
 * @date   April 2020
 * This CPP program is an application example of openMP for large matrix multiplication
 */

//including necessary header files
#include <iostream>

#ifdef _OPENMP
#include <omp.h>
#endif

#include <cstring>

using namespace std;

//setting the maximum allowed size of matrices
#define max_dim_lim 2500

//declaring necessary matrices
int p[max_dim_lim][max_dim_lim];
int q[max_dim_lim][max_dim_lim];
int r[max_dim_lim][max_dim_lim];
int s[max_dim_lim][max_dim_lim];


//generating random matrices
void populateMatrix(int N)
{
	int i,j;
	for(i=0;i<N;i++)
	{
		for(j=0;j<N;j++)
		{
			p[i][j]=rand()%100;
			q[i][j]=rand()%100;
		}
	}
}

//a function to compare the product of naive method to openMP approaches
int compareMatrices(int N)
{
	int i,j;
	for(i=0;i<N;i++)
		for(j=0;j<N;j++)
			if(r[i][j]!=s[i][j]) 
				return 0;
	return 1;
}

//Naive method of matrix multiplication
void matrixMulNaive(int N)
{
	int i,j,k;
	double start=omp_get_wtime();
	try {
	    for(i=0;i<N;i++)
	    {
	    	for(j=0;j<N;j++)
	    	{
	    		for(k=0;k<N;k++)
	    		{
	    			r[i][j]+=p[i][k]*q[k][j];
	    		}
	    	}
	    }
	    double end=omp_get_wtime();
	    cout << "Elapsed time for the Naive Multiplication:\t\t\t\t\t\t" << end-start<< "\n\n";
	}
	catch(...){
		cout<< "Naive multiplication section failed";
	}
	
}

//matrix multiplication with openMP on for loop - IJK
void matrixMulParallelFor_ijk(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	
	try {
	    #pragma omp parallel for private(i,j,k)shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                for (k = 0; k < N; k++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
	    double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop - IJK:\t\t\t" << end-start<< "\n";
		if(compareMatrices(N))
	    	cout<<"Product matrix of the for - IJK section is equal to the output of Naive method \n\n";	
	    else
	    	cout<<"Product matrix of the for - IJK section is not equal to the output of Naive method\n\n";
	}
	catch(...){
	    cout<< "OpenMP For loop - IJK section failed";
	}
}

//matrix multiplication with openMP on for loop - IKJ
void matrixMulParallelFor_ikj(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	
	try {
	    #pragma omp parallel for private(i,k,j)shared(p,q,r)
	    for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }		
	    double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop - IKJ:\t\t\t" << end-start<< "\n";
		if(compareMatrices(N))
	    	cout<<"Product matrix of the for - IKJ section is equal to the output of Naive method \n\n";	
	    else
	    	cout<<"Product matrix of the for - IKJ section is not equal to the output of Naive method\n\n";
	}
	catch(...){
	    cout<< "OpenMP For loop - IKJ section failed";
	}
}
                   //openMP schedule section for IKJ starts here
//matrix multiplication with openMP on for loop and schedule - static
void matrixMulParallelScheduleStatic(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	
	try {
	    //static is default parameter for schedule, so we don't have to write it
	    #pragma omp parallel for schedule(simd:static) collapse(2) private(i,k,j)shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
	    double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop, Static schedule:\t" << end-start<< "\n";
		if(compareMatrices(N))
	    	cout<<"Product matrix of the schedule - static section is equal to the output of Naive method \n\n";	
	    else
	    	cout<<"Product matrix of the schedule - static section is not equal to the output of Naive method \n\n";
	}
	catch(...){
	    cout<< "Static section failed";
	}
}
                       
//matrix multiplication with openMP on for loop and schedule - dyanamic
void matrixMulParallelScheduleDynamic(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	
	try {
	    #pragma omp parallel for schedule(simd:dynamic) private(i,k,j) shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
		double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop, Dynamic schedule:\t" << end-start<< "\n";
		if(compareMatrices(N))
		cout<<"Product matrix of the schedule - dynamic section is equal to the output of Naive method \n\n";	
	    else
		cout<<"Product matrix of the schedule - dynamic section is not equal to the output of Naive method \n\n";
	}
	catch(...){
		cout<< "Dynamic section failed";
	}
}

//matrix multiplication with openMP on for loop and schedule - guided
void matrixMulParallelScheduleGuided(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	try {
	    #pragma omp parallel for schedule(simd:guided) private(i,k,j) shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
		double end=omp_get_wtime();
		cout << "Elapsed time for matrix multiplication with openMP on for loop, Guided schedule:\t" << end-start << "\n";
		if(compareMatrices(N))
		cout<<"Product matrix of the schedule - guided section is equal to the output of Naive method \n\n";	
	    else
		cout<<"Product matrix of the schedule - guided section is not equal to the output of Naive method \n\n";
	}
	catch(...){
		cout<< "Guided section failed";
	}
}

//matrix multiplication with openMP on for loop and schedule - auto
void matrixMulParallelScheduleAuto(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	try {
	    #pragma omp parallel for schedule(simd:auto) collapse(2) private(i,k,j) shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
		double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop, Auto schedule:\t\t" << end-start << "\n";
		if(compareMatrices(N))
		cout<<"Product matrix of the schedule - auto section is equal to the output of Naive method \n\n";	
	    else
		cout<<"Product matrix of the schedule - auto section is not equal to the output of Naive method \n\n";
	}
	catch(...){
		cout<< "Auto section failed";
	}
}

//matrix multiplication with openMP on for loop and schedule - runtime
void matrixMulParallelScheduleRuntime(int N)
{
	memset(s,0,sizeof s);
	int i,j,k;
	double start=omp_get_wtime();
	try {
	    #pragma omp parallel for schedule(simd:runtime) private(i,k,j) shared(p,q,r)
		for (i = 0; i < N; i++) {
            for (k = 0; k < N; k++) {
                for (j = 0; j < N; j++) {
                     s[i][j] += p[i][k] * q[k][j];
                }
            }
        }
		double end=omp_get_wtime();
	    cout << "Elapsed time for matrix multiplication with openMP on for loop, Runtime schedule:\t" << end-start << "\n";
	    if(compareMatrices(N))
		cout<<"Product matrix of the schedule - runtime section is equal to the output of Naive method \n\n";	
	    else
		cout<<"Product matrix of the schedule - runtime section is not equal to the output of Naive method \n\n";
	}
	catch(...){
		cout<< "Runtime section failed";
	}
}

int main() {
	int N;
	cout<< "\t\t\tHello!\n" <<"Welcome to the Large Matrix Multiplication Program.\n";
	cout<< "We have set the maximum dimensions of the matrices as 2500*2500.\n";
	cout<<"\nNow, please enter the value of the dimension for which you want a matrix multiplication\n\n";
	cin>>N;
	cout<<"\n";
	
	if(N>2500){
		cout<<"The entered dimension size exceeded the allowed limit.\n";
		cout<<"Terminating the session...";
		return 1;
	}
	else{
	    populateMatrix(N);
	    matrixMulNaive(N);	
	    matrixMulParallelFor_ijk(N);
        matrixMulParallelFor_ikj(N);
	    matrixMulParallelScheduleStatic(N);
        matrixMulParallelScheduleDynamic(N);
		matrixMulParallelScheduleGuided(N);
		matrixMulParallelScheduleAuto(N);
		matrixMulParallelScheduleRuntime(N);
		cout<<"\nProgram executed successfully...";

	return 0;
	}
	
}
