package ${package}.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${package}.model.DatoEjemplo;
import ${package}.model.DatoEjemploMongo;
import ${package}.model.HolaMundo;
import ${package}.repo.HolaMundoRepo;
import ${package}.repo.mongo.MongoRepoEjemplo;

/**
 * 
 * @author Cristian Gonzalez 8 ago. 2019
 *
 *         En la capa de servicios debe implmentarse toda la logica de
 *         navegacion
 */
@Service
public class ApiEjemploService {

   
    @Autowired private HolaMundoRepo repo;
    
    @Autowired private MongoRepoEjemplo mongoRepo;
     
    /** EJemplo basico de la capa de servicos
     * 
     * @return
     */
    public HolaMundo getHolaMundo() {
	return new HolaMundo(LocalDateTime.now());
    }

     public void insertarDato(DatoEjemplo dato) {
	 repo.insertarEjemplo(dato);
     }
     
     /**
      * Ejemplo de guardado en mongoDB
      * @param ejemplo
      * @return
      */
     public DatoEjemploMongo guardarDato(DatoEjemploMongo ejemplo) {
	 mongoRepo.save(ejemplo);
	 return ejemplo;
     }
}
