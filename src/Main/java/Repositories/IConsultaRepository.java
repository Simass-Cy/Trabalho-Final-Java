package Repositories;

import Entities.Animais;
import Entities.Consulta;
import Entities.Funcionario;
import Exceptions.RepositoryException;

import java.time.LocalDate;
import java.util.List;

public interface IConsultaRepository {

    void salvarConsulta(Consulta consulta) throws RepositoryException;

    Consulta buscarPorIdConsulta(long id) throws RepositoryException;

    List<Consulta> buscarPorAnimalConsulta(Animais animal) throws RepositoryException;

    List<Consulta> buscarPorVeterinario(Funcionario veterinario) throws RepositoryException;

    List<Consulta> buscarPorPeriodoConsulta(LocalDate dataInicio, LocalDate dataFim) throws RepositoryException;

}