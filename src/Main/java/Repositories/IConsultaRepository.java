package Repositories;

import Entities.Animais;
import Entities.Consulta;
import Entities.Funcionario;
import java.time.LocalDate;
import java.util.List;

public interface IConsultaRepository {

    void salvarConsulta(Consulta consulta);

    Consulta buscarPorIdConsulta(long id);

    List<Consulta> buscarPorAnimalConsulta(Animais animal);

    List<Consulta> buscarPorVeterinario(Funcionario veterinario);

    List<Consulta> buscarPorPeriodoConsulta(LocalDate dataInicio, LocalDate dataFim);

}