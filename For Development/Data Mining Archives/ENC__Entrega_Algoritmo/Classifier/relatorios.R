
R1=data.frame(clientes[,1],Prob[,1:3],Q1[,1:3])
colnames(R1) <- c("ID","Probabilidade 1","Probabilidade 2","Probabilidade 3","Distancia 1","Distancia 2","Distancia 3")

R2=data.frame(unlist(F1),unlist(F2),unlist(F3))
colnames(R2) <- c("Persona 1","Persona 2","Persona 3")

R=data.frame(R1,R2)

write.csv2(R,"Relatorio.csv")

