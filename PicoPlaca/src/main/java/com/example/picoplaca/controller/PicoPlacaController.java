package com.example.picoplaca.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class PicoPlacaController {

    @GetMapping("/checkPicoPlaca")
    public ResponseEntity<String> checkPicoPlaca(
            @RequestParam String plateNumber,
            @RequestParam String dateTime) {
        
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Error en el formato de fecha, consulte su formato de fecha local.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (date.isBefore(now)) {
            return ResponseEntity.badRequest().body("La fecha debe ser posterior a la fecha actual.");
        }

        boolean canCirculate = checkPicoPlacaLogic(plateNumber, date);

        if (canCirculate) {
            return ResponseEntity.ok("El vehículo con placa " + plateNumber + " puede circular en la fecha y hora ingresada.");
        } else {
            return ResponseEntity.ok("El vehículo con placa " + plateNumber + " NO puede circular en la fecha y hora ingresada.");
        }
    }

    private boolean checkPicoPlacaLogic(String plateNumber, LocalDateTime date) {
        int lastDigit = Character.getNumericValue(plateNumber.charAt(plateNumber.length() - 1));
        int dayOfWeek = date.getDayOfWeek().getValue();
        
        if ((dayOfWeek == 1 && (lastDigit == 1 || lastDigit == 2)) ||
            (dayOfWeek == 2 && (lastDigit == 3 || lastDigit == 4)) ||
            (dayOfWeek == 3 && (lastDigit == 5 || lastDigit == 6)) ||
            (dayOfWeek == 4 && (lastDigit == 7 || lastDigit == 8)) ||
            (dayOfWeek == 5 && (lastDigit == 9 || lastDigit == 0))) {
            return false;
        }

        return true;
    }
}
