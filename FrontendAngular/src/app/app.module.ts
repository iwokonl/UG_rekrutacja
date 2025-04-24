import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import {HomeComponent} from './home/home.component';
import {RouterModule, Routes} from '@angular/router';
import { SearchbarComponent } from './searchbar/searchbar.component';
import {FormsModule} from '@angular/forms';

const routes: Routes = [
  { path: '', component: SearchbarComponent }, // Strona główna
  { path: '**', redirectTo: '' } // Przekierowanie na stronę główną dla nieznanych ścieżek
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent
  ],
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    MatToolbarModule,
    RouterModule.forRoot(routes),
    SearchbarComponent
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
