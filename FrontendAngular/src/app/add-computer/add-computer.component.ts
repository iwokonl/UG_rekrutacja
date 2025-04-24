import {Component} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';
import {Router} from '@angular/router';
import {AxiosService} from '../services/axios.service';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {MatButton} from '@angular/material/button';
import {FlexLayoutModule} from '@angular/flex-layout';

@Component({
  selector: 'app-add-computer',
  templateUrl: './add-computer.component.html',
  imports: [
    MatDatepickerToggle,
    MatDatepicker,
    MatFormFieldModule,
    FormsModule,
    FlexLayoutModule,
    MatDatepickerInput,
    MatInput,
    MatButton
  ],
  styleUrls: ['./add-computer.component.css']
})
export class AddComputerComponent {
  computer = {
    name: '',
    currencyConversionDate: '',
    postingDate: '',
    priceInUsd: ''
  };

  constructor(private axiosService: AxiosService, private router: Router) {
  }

  async onSubmit(form: NgForm) {
    if (form.valid) {
      try {
        const response = await this.axiosService.post('/computer/saveComputer', this.computer);
        console.log('Komputer został dodany:', response);
        form.resetForm();
        this.router.navigate(['/']); // Przeniesienie na główną stronę
      } catch (error) {
        console.error('Błąd podczas dodawania komputera:', error);
      }
    }
  }
}
