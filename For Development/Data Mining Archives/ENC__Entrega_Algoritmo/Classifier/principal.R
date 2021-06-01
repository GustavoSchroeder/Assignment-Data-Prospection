source("distancias.R")
source("analisa.R")

#Importa clientes
#clientes <- read.csv2("Base Teste.csv", encoding="UTF-8")
clientes <- read.csv2("Base Integrada.csv", encoding="UTF-8")

# Importa personas
personas <- read.csv2("Personas Associadas.csv", encoding="UTF-8")

# Cria matrizes

N <- nrow(clientes)
M <- nrow(personas)

T  <- matrix(0,N,M)
Q1 <- matrix(0,N,M)
Q2 <- matrix(0,N,M)
Q2_base <- matrix(0,N,M)

F1 <- list()
F2 <- list()
F3 <- list()

# Processa personas dados base

qual = matrix(0,1,10)
qual[1] = 1
qual[2] = 1
qual[7] = 1

for(i in 1:N) {
  print(i)
  C = clientes[i,]
  for(j in 1:M) {
    P = personas[j,]
    D = distancia(C,P)
    T[i,j] = qual%*%D
  }
  Q1[i,]=sort(T[i,], index.return=TRUE)$x
  Q2_base[i,]=sort(T[i,], index.return=TRUE)$ix
  F1[i] = personas[Q2_base[i,1],2]
  F2[i] = personas[Q2_base[i,2],2]
  F3[i] = personas[Q2_base[i,3],2]
}

Prob = probabilidade(Q1)

# Processa personas todos dados

qual = matrix(1,1,10)
qual[1] = 1
qual[2] = 1
qual[7] = 1

for(i in 1:N) {
  print(i)
  C = clientes[i,]
  for(j in 1:M) {
    P = personas[j,]
    D = distancia(C,P)
    T[i,j] = qual%*%D
  }
  Q1[i,]=sort(T[i,], index.return=TRUE)$x
  Q2[i,]=sort(T[i,], index.return=TRUE)$ix
  F1[i] = personas[Q2[i,1],2]
  F2[i] = personas[Q2[i,2],2]
  F3[i] = personas[Q2[i,3],2]
}

Prob = probabilidade(Q1)


source("relatorios.R")