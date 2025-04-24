// app.module.ts
import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {registerLocaleData} from '@angular/common';
import localePl from '@angular/common/locales/pl';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {FooterComponent} from './footer/footer.component';
import {HomeComponent} from './home/home.component';
import {SearchbarComponent} from './searchbar/searchbar.component';

import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatDatepickerModule} from '@angular/material/datepicker';

import {MatMomentDateModule} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_LOCALE} from '@angular/material/core';
import {PolishDateAdapter} from './adapter/custom-date-adapter';
import {MatSortModule} from '@angular/material/sort';
import {MatAnchor} from '@angular/material/button';
import {AddComputerComponent} from './add-computer/add-computer.component';


registerLocaleData(localePl);

// 2) Definicja formatu dat dla MomentAdapter
export const POLISH_MOMENT_FORMATS = {
  parse: {
    dateInput: 'DD.MM.YYYY',
  },
  display: {
    dateInput: 'DD.MM.YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

const routes: Routes = [
  {path: '', component: SearchbarComponent},
  {path: 'searchbar', component: SearchbarComponent},
  // { path: '**', redirectTo: '' },
  {path: 'addComputer', component: AddComputerComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,

    // <-- tu dodajemy komponent
  ],
  imports: [
    MatFormFieldModule,
    MatSortModule,
    BrowserModule,
    AppRoutingModule,
    RouterModule.forRoot(routes),

    // Angular Material
    MatToolbarModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatDatepickerModule,
    MatMomentDateModule,   // <-- Moment adapter

    FormsModule,
    SearchbarComponent,
    MatAnchor,
    AddComputerComponent,
  ],
  providers: [
    {provide: LOCALE_ID, useValue: 'pl-PL'},
    {provide: MAT_DATE_LOCALE, useValue: 'pl-PL'},
    {provide: DateAdapter, useClass: PolishDateAdapter}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
