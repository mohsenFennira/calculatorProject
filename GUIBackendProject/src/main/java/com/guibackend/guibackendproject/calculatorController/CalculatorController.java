package com.guibackend.guibackendproject.calculatorController;

import com.guibackend.guibackendproject.DTOs.CalculationResponse;
import com.guibackend.guibackendproject.DTOs.ExpressionRequest;
import com.guibackend.guibackendproject.calculatorService.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin("*")
public class CalculatorController {
    @Autowired
    CalculatorService calculatorService;
    @PostMapping("/evaluate")
    public ResponseEntity<CalculationResponse> evaluate(@RequestBody ExpressionRequest request) {
        try {
            double result = calculatorService.evaluateExpression(request.getExpression(), request.getType());
            return ResponseEntity.ok(new CalculationResponse(result, "Success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new CalculationResponse(0, e.getMessage()));
        }
    }

    @GetMapping("/limitations")
    public String getLimitations() {
        return "Obsługiwane operacje: +, -, *, /, ^ | Dozwolone tylko liczby | Bez zmiennych ani funkcji | Obsługiwane ONP lub algebraiczne";
    }

}
