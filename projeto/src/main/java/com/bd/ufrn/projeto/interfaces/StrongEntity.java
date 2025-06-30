package com.bd.ufrn.projeto.interfaces;

public interface StrongEntity<Type,Id> {
    //SELECT * FROM <TYPENAME> WHERE <id> = id
    //Pra strings usar LIKE talvez?
    //Movido pra essa interface pra facilitar a vida com entidades fracas
    abstract public Type findById(Id id);
}
