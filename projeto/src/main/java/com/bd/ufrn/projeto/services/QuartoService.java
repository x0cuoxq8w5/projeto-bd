package com.bd.ufrn.projeto.services;

import com.bd.ufrn.projeto.dtos.QuartoDTO;
import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.interfaces.CrudService;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.repositories.QuartoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService implements CrudService<Quarto, QuartoDTO,Integer> {
    @Autowired private QuartoRepository quartoRepository;

    @Override
    public Quarto get(Integer id) {
        return quartoRepository.findById(id);
    }

    @Override
    public void create(QuartoDTO quartoDTO) {
        Quarto quarto = Quarto.builder()
                .numero(quartoDTO.numero())
                .tipo(quartoDTO.tipo())
                .naoPerturbe(false)
                .ocupado(false)
                .marcadoParaLimpeza(false)
                .build();
        quartoRepository.save(quarto);
    }

    @Override
    public void update(Integer id, QuartoDTO quartoDTO) {
            Quarto quarto = get(id);
            if(quartoDTO.tipo()!=null){
                quarto.setTipo(quartoDTO.tipo());
            }
            if(quartoDTO.naoPerturbe() != null){
                quarto.setNaoPerturbe(quartoDTO.naoPerturbe());
            }
            if(quartoDTO.ocupado() != null){
                quarto.setOcupado(quartoDTO.ocupado());
            }
            if(quartoDTO.marcadoPraLimpeza() != null){
                quarto.setMarcadoParaLimpeza(quartoDTO.marcadoPraLimpeza());
            }
            quartoRepository.save(quarto);
    }

    @Override
    public void delete(Integer id) {
        Quarto quarto = get(id);
        quartoRepository.delete(quarto);
    }

    @Override
    public List<Quarto> getAll() {
        return quartoRepository.findAll();
    }

    public List<Quarto> getByOcupado() {
        return quartoRepository.findByOcupado();
    }

    public List<Quarto> getByMarcadoParaLimpeza() {
        return quartoRepository.findByMarcado();
    }

    public List<Quarto> findByTipo(String tipo) {
        return quartoRepository.findByTipo(TipoQuarto.valueOf(tipo));
    }
}
