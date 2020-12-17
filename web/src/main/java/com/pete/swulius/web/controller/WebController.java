package com.pete.swulius.web.controller;

import com.pete.swulius.web.model.CityState;
import com.pete.swulius.web.model.CityStateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Comparator;
import java.util.List;


@Controller
class WebController {

    List<CityState> cities;

    @Autowired
    private Repository service;


    @RequestMapping(value = "/")
    public String home() {
        return "home";
    }


    @GetMapping("/cities")
    public String cities(Model model) {
        List<CityState> cities = getCities();
        model.addAttribute("cities",cities);
        return "cities";
    }

    @GetMapping("/citylogs/{city}/{state}")
    public String citylogs(
            @PathVariable(value = "city") String aCity,
            @PathVariable(value = "state") String aState,
                           Model model) {

        CityState cs = new CityState();
        cs.city = aCity;
        cs.state = aState;
        model.addAttribute("cs", cs);
        model.addAttribute("csfull", aCity + ", " + aState);

        List<CityStateLog> logs = getLogsForCity(aCity,aState);
        model.addAttribute("logs", logs );

        return "citylogs";
    }

    // --------------------
    // private methods
    // --------------------
    private synchronized List<CityState> getCities() {
        if( cities == null ) {
            cities = service.findAllCityStates().collectList().block();
            cities.sort(new Comparator<CityState>() {
                @Override
                public int compare(CityState o1, CityState o2) {
                    String f1 = o1.state + o1.city;
                    String f2 = o2.state + o2.city;
                    return f1.compareTo(f2);
                }
            });
        }
        return cities;
    }

    private List<CityStateLog> getLogsForCity( String aCity, String aState )
    {
        return service.findLogsForCityState(aCity,aState);
    }

}
