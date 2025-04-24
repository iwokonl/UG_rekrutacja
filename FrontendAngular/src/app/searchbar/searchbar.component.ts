import {Component, OnInit} from '@angular/core';
import {AxiosService} from '../services/axios.service';
import {FormsModule} from '@angular/forms';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {CommonModule, DatePipe} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from '@angular/material/table';
import {Subject} from 'rxjs';
import {debounceTime} from 'rxjs/operators';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatDatepicker,
    DatePipe,
    // MatIconButton,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef,
  ],
  styleUrls: ['./searchbar.component.css'],
  providers: [DatePipe]
})
export class SearchbarComponent implements OnInit {
  displayedColumns: string[] = ['name', 'priceInPln', 'priceInUsd', 'postingDate', 'currencyConversionDate'];
  searchQuery: string = '';
  dateFrom: string = '';
  dateTo: string = '';
  items: any[] = [];
  filteredItems: any[] = [];
  private searchSubject: Subject<void> = new Subject<void>();

  constructor(private axiosService: AxiosService, private datePipe: DatePipe) {
  }

  async ngOnInit() {
    this.searchSubject.pipe(debounceTime(350)).subscribe(() => {
      this.fetchItems();
    });
    await this.fetchItems();
  }

  async fetchItems() {
    try {
      const params: Record<string, any> = {};
      if (this.searchQuery) params['name'] = this.searchQuery;
      if (this.dateFrom) {
        params['dateFrom'] = this.datePipe.transform(this.dateFrom, 'yyyy-MM-dd');
      }
      if (this.dateTo) {
        params['dateTo'] = this.datePipe.transform(this.dateTo, 'yyyy-MM-dd');
      }

      this.items = await this.axiosService.getWithParams('/computer/searchByNameFragmentAndDateRangePosting', params);
      this.filteredItems = this.items;
      this.updatePrices();
    } catch (error: any) {
      console.error('Błąd podczas pobierania danych:', error);
      this.filteredItems = [];
    }
  }

  onSearch() {
    this.searchSubject.next();
  }

  formatPrice(price: any): string {
    if (typeof price === 'number') {
      return price.toFixed(2);
    }
    if (typeof price === 'string' && !price.includes('.')) {
      return `${price}.00`;
    }
    return price;
  }

  updatePrices() {
    this.filteredItems = this.filteredItems.map(item => ({
      ...item,
      priceInPln: this.formatPrice(item.priceInPln),
      priceInUsd: this.formatPrice(item.priceInUsd),
    }));
  }
}
