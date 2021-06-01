probabilidade <- function(Q1){

  Prob = matrix(0,N,3)

  for(i in seq(1,N,1)) {
  
    linha = Q1[i,1:3] 

    if (min(linha)==0){
  
      for(j in seq(1,3,1)) {
        Prob[i,j]=0
      }
      Prob[i,which.min(linha)]=1

    }else{

      for(j in seq(1,3,1)) {
        Prob[i,j]=1/linha[j]
      }
    
    }
    
    Prob[i,]=Prob[i,]/sum(Prob[i,])
  
  }

  return(Prob)
  write.csv2(Prob,"probabilidade.csv")
  
}  

incerteza <- function(){

  D1=0
  D2=0
  D3=0
  D4=0
  D5=0
  for(i in seq(1,N,1)) {
  
    if (Q2[i,1]==Q2_base[i,1] | Q2[i,1]==Q2_base[i,2] | Q2[i,1]==Q2_base[i,3]){
      D1=D1+1
    }
    if (Q2[i,2]==Q2_base[i,1] | Q2[i,1]==Q2_base[i,2] | Q2[i,1]==Q2_base[i,3]){
      D2=D2+1
    }
    if (Q2[i,3]==Q2_base[i,1] | Q2[i,1]==Q2_base[i,2] | Q2[i,1]==Q2_base[i,3]){
      D3=D3+1
    }
    if (Q2[i,1]==Q2_base[i,1] ){
      D4=D4+1
    }
    if (Q2[i,1]==Q2_base[i,1] & Q2[i,2]==Q2_base[i,2] & Q2[i,3]==Q2_base[i,3]){
      D5=D5+1
    }
    
      
  }
    
  return(list(D1,D2,D3,D4,D5))
}


analise <- function(i,j){
  
  C = clientes[i,]
  P = personas[j,]
  D = distancia(C,P)
  
  nome = clientes[i,2]
  print(sprintf("Nome: %s",nome))
  id = clientes[i,1]
  print(sprintf("ID: %s",id))
  
  persona = personas[j,2]
  print(sprintf("Persona: %s",persona))
  
  print(sprintf("Idade: %f",D[1]))
  print(sprintf("Renda: %f",D[2]))
  print(sprintf("Ocupação: %f",D[3]))
  print(sprintf("Maturidade: %f",D[4]))
  print(sprintf("Escolaridade: %f",D[5]))
  print(sprintf("Com quem mora: %f",D[6]))
  print(sprintf("Produtos: %f",D[7]))
  print(sprintf("Pergunta 1: %f",D[8]))
  print(sprintf("Pergunta 2: %f",D[9]))
  print(sprintf("Pergunta 3: %f",D[10]))
  
  DT=qual%*%distancia(C,P)
  
  return(DT[1])
}

analise_cliente <- function(i){
  
  C <- clientes[i,]
  R <- matrix(0,10,14)
  
  #campos <- list("Idade","Renda","Ocupação","Maturidade","Escolaridade","Com quem mora","Produtos","Pergunta 1","Pergunta 2","Pergunta 3")
  
  for(j in 1:M) {
    P = personas[j,]
    D = distancia(C,P)
    #DT=qual%*%distancia(C,P)
    R[,j]=D
  }
  return(cbind(campos,R))
}

