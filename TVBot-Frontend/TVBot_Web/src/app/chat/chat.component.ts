import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';


interface TVShow {
  title: string;
  genre: string;
  description: string;
}

interface RecommendationResponse {
  summary: string;
  shows: TVShow[];
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})


export class ChatComponent {
prompt = '';
  response: RecommendationResponse | null = null;
  loading = false;
  error = '';

  constructor(private http: HttpClient) {}

  getRecommendations() {
    this.loading = true;
    this.response = null;
    this.error = '';
    const body = { prompt: this.prompt };

    this.http.post<RecommendationResponse>(
      'http://localhost:8080/api/tvshows/recommend?count=1', // Adjust port if needed
      body
    ).subscribe({
      next: (res) => {
        this.response = res;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to get recommendations. Please try again.';
        this.loading = false;
      }
    });
  }
}
