package ${package}.api_ejemplo.repo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import ${package}.exceptions.TrabajandoException;
import ${package}.mappers.HolaMundoMapper;
import ${package}.model.DatoEjemplo;

@Repository("nominaRepo")
public class HolaMundoRepo {
    
    private static Logger logger = LogManager.getLogger(HolaMundoRepo.class);
    @Autowired
    private HolaMundoMapper mapper;

    public void insertarEjemplo(DatoEjemplo dato) {
	try {
	    mapper.insertarEjemplo(dato);;
	} catch (DataAccessException ex) {
	    logger.error("Error de acceso de datos - getHolaMundo",ex);
	    throw new TrabajandoException(ex);
	}
    }

}
