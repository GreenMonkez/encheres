package fr.eni.tp.encheres.controller.converter;

import org.springframework.core.convert.converter.Converter;

import fr.eni.tp.encheres.bll.EnchereService;
import fr.eni.tp.encheres.bo.Categorie;

public class StringToCategorieConverter implements Converter<String, Categorie> {

	private EnchereService enchereService;

	public StringToCategorieConverter(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@Override
	public Categorie convert(String idCategorie) {
		return enchereService.getCategorie(Integer.parseInt(idCategorie));
	}
}
