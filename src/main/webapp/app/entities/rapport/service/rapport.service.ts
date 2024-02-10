import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRapport, NewRapport } from '../rapport.model';

export type PartialUpdateRapport = Partial<IRapport> & Pick<IRapport, 'id'>;

export type EntityResponseType = HttpResponse<IRapport>;
export type EntityArrayResponseType = HttpResponse<IRapport[]>;

@Injectable({ providedIn: 'root' })
export class RapportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rapports');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(rapport: NewRapport): Observable<EntityResponseType> {
    return this.http.post<IRapport>(this.resourceUrl, rapport, { observe: 'response' });
  }

  update(rapport: IRapport): Observable<EntityResponseType> {
    return this.http.put<IRapport>(`${this.resourceUrl}/${this.getRapportIdentifier(rapport)}`, rapport, { observe: 'response' });
  }

  partialUpdate(rapport: PartialUpdateRapport): Observable<EntityResponseType> {
    return this.http.patch<IRapport>(`${this.resourceUrl}/${this.getRapportIdentifier(rapport)}`, rapport, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRapport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRapport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRapportIdentifier(rapport: Pick<IRapport, 'id'>): number {
    return rapport.id;
  }

  compareRapport(o1: Pick<IRapport, 'id'> | null, o2: Pick<IRapport, 'id'> | null): boolean {
    return o1 && o2 ? this.getRapportIdentifier(o1) === this.getRapportIdentifier(o2) : o1 === o2;
  }

  addRapportToCollectionIfMissing<Type extends Pick<IRapport, 'id'>>(
    rapportCollection: Type[],
    ...rapportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rapports: Type[] = rapportsToCheck.filter(isPresent);
    if (rapports.length > 0) {
      const rapportCollectionIdentifiers = rapportCollection.map(rapportItem => this.getRapportIdentifier(rapportItem)!);
      const rapportsToAdd = rapports.filter(rapportItem => {
        const rapportIdentifier = this.getRapportIdentifier(rapportItem);
        if (rapportCollectionIdentifiers.includes(rapportIdentifier)) {
          return false;
        }
        rapportCollectionIdentifiers.push(rapportIdentifier);
        return true;
      });
      return [...rapportsToAdd, ...rapportCollection];
    }
    return rapportCollection;
  }
}
