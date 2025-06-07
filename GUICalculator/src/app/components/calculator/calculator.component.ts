import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-calculator',
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './calculator.component.html',
  styleUrl: './calculator.component.css'
})
export class CalculatorComponent {
  title = 'GUICalculator';
  expression = '';
  type: 'algebraic' | 'rpn' = 'algebraic';
  result: number | null = null;
  message = '';
  limitations = '';

  constructor(private http: HttpClient) {}

  calculate() {
    this.http.post<any>('http://localhost:8085/api/calculator/evaluate', {
      expression: this.expression,
      type: this.type
    }).subscribe({
      next: res => {
        this.result = res.result;
        this.message = '';
      },
      error: err => {
        this.message = err.error.message || 'Wystąpił błąd';
        this.result = null;
      }
    });
  }

  showLimitations() {
    this.http.get('http://localhost:8085/api/calculator/limitations', { responseType: 'text' }).subscribe(
      data => this.limitations = data
    );
  }

  clear() {
    this.expression = '';
    this.result = null;
    this.message = '';
    this.limitations = '';
  }

  checkCorrectness() {
    this.http.post<any>('http://localhost:8085/api/calculator/check', {
      expression: this.expression,
      type: this.type
    }).subscribe({
      next: res => {
        this.message = res.message;
        this.result = null;
      },
      error: err => {
        this.message = err.error.message || 'Wystąpił błąd';
        this.result = null;
      }
    });
  }

  convertToRPN() {
    this.http.post<any>('http://localhost:8085/api/calculator/convert-to-rpn', {
      expression: this.expression
    }).subscribe({
      next: res => {
        this.expression = res.expression;
        this.type = 'rpn';
        this.message = 'Przekonwertowano do ONP';
      },
      error: err => {
        this.message = err.error.message || 'Wystąpił błąd';
      }
    });
  }

  convertFromRPN() {
    this.http.post<any>('http://localhost:8085/api/calculator/convert-from-rpn', {
      expression: this.expression
    }).subscribe({
      next: res => {
        this.expression = res.expression;
        this.type = 'algebraic';
        this.message = 'Przekonwertowano na algebraiczne';
      },
      error: err => {
        this.message = err.error.message || 'Wystąpił błąd';
      }
    });
  }
}
