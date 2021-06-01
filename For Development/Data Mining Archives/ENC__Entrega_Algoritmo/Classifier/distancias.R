# Calcula distancias

distancia <- function(C,P){

  distancia_total = c(
  3*idade(C,P)/13,
  1*renda(C,P)/3000,
  2*ocupacao(C,P),
  0.5*maturidade(C,P)/0.63,
  2*escolaridade(C,P),
  1*com_quem_mora(C,P)/2,
  1*produtos(C,P)/10,
  1*crescimento_profissional(C,P)/3,
  1*conhecimento_investimento(C,P)/3,
  1*guarda_ou_investe(C,P)/3)

  return(distancia_total)
}

idade <- function(C,P){

  cliente = C[,c("IDADE")]
  min_persona = P[,c("IDADE_MIN")]
  max_persona = P[,c("IDADE_MAX")]
  distancia = back(cliente,min_persona,max_persona)

}

renda <- function(C,P){
  
  cliente = C[,c("RENDA_MENSAL")]
  min_persona = P[,c("RENDA_MIN")]
  max_persona = P[,c("RENDA_MAX")]
  distancia = back(cliente,min_persona,max_persona)

}


ocupacao <- function(C,P){
  
  cliente = C[13:22]
  persona = P[18:27]
  
  if (sum(cliente*persona)==0){
    distancia = 1
  }else{
    distancia = 0
  }

}

maturidade <- function(C,P){
  
  cliente = C[,c("MATURIDADE_PROFISSIONAL")]
  persona = P[,c("MATURIDADE_PROFISSIONAL")]
  
  if (cliente==""){
    distancia = 0
  }else{
    if (cliente=="Menos de 1 ano") cliente=1
    if (cliente=="Entre 1 e 5 anos") cliente=2
    if (cliente=="Mais de 5 anos") cliente=3
    distancia = abs(persona - cliente)
  }

  return(distancia)
  
}

escolaridade <- function(C,P){
  
  cliente = C[4:8]
  persona = P[5:9]
  distancia = 1 - sum(cliente*persona)

}

com_quem_mora <- function(C,P){
  
  cliente = C[24:28]
  persona = P[29:33]
  distancia = sum(abs(cliente-persona))

}

produtos <- function(C,P){
  
  cliente = C[29:51]
  persona = P[34:56]
  distancia = sum(abs(cliente-persona))

}

crescimento_profissional <- function(C,P){
  
  cliente = C[,c("CRESCIMENTO_PROFISSIONAL")]
  min_persona = P[,c("CRESCIMENTO_PROFISSIONAL_MIN")]
  max_persona = P[,c("CRESCIMENTO_PROFISSIONAL_MAX")]
  
  if (is.na(cliente)){
    distancia=0
  }else{
    distancia = back(cliente,min_persona,max_persona)
  }
  
}

conhecimento_investimento <- function(C,P){
  
  cliente = C[,c("CONHECIMENTO_INESTIMENTOS")]
  min_persona = P[,c("CONHECIMENTO_INESTIMENTOS_MIN")]
  max_persona = P[,c("CONHECIMENTO_INESTIMENTOS_MAX")]
  
  if (is.na(cliente)){
    distancia=0
  }else{
    distancia = back(cliente,min_persona,max_persona)
  }

  }

guarda_ou_investe <- function(C,P){
  
  cliente = C[,c("GUARDA_OU_INVESTE")]
  min_persona = P[,c("GUARDA_OU_INVESTE_MIN")]
  max_persona = P[,c("GUARDA_OU_INVESTE_MAX")]
  
  if (is.na(cliente)){
    distancia=0
  }else{
    distancia = back(cliente,min_persona,max_persona)
  }
  
}

back <- function(a,b,c){
  if (a > c){
    d = a-c
  }else if (a < b){
    d = b - a
  }else{
    d = 0
  }
}