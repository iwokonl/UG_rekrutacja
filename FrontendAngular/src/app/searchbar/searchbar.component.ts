import { Component, OnInit } from '@angular/core';
import { AxiosService } from '../services/axios.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  imports: [
    FormsModule
  ],
  styleUrls: ['./searchbar.component.css']
})
export class SearchbarComponent implements OnInit {
  searchQuery: string = '';
  dateFrom: string = '';
  dateTo: string = '';
  items: any[] = [];
  filteredItems: any[] = [];

  constructor(private axiosService: AxiosService) {}

  async ngOnInit() {
    await this.fetchItems();
  }

  async fetchItems() {
    try {
      const params: Record<string, any> = {};
      if (this.searchQuery) params['name'] = this.searchQuery;
      if (this.dateFrom) params['dateFrom'] = this.dateFrom;
      if (this.dateTo) params['dateTo'] = this.dateTo;

      this.items = await this.axiosService.getWithParams('/computer/searchByNameFragmentAndDateRangePosting', params);
      this.filteredItems = this.items;
    } catch (error) {
      console.error('Błąd podczas pobierania danych:', error);
    }
  }

  onSearch() {
    this.fetchItems();
  }
}
