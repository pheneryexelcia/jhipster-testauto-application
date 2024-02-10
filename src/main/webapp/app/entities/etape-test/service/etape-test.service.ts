import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtapeTest, NewEtapeTest } from '../etape-test.model';

export type PartialUpdateEtapeTest = Partial<IEtapeTest> & Pick<IEtapeTest, 'id'>;

export type EntityResponseType = HttpResponse<IEtapeTest>;
export type EntityArrayResponseType = HttpResponse<IEtapeTest[]>;

@Injectable({ providedIn: 'root' })
export class EtapeTestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etape-tests');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(etapeTest: NewEtapeTest): Observable<EntityResponseType> {
    return this.http.post<IEtapeTest>(this.resourceUrl, etapeTest, { observe: 'response' });
  }

  update(etapeTest: IEtapeTest): Observable<EntityResponseType> {
    return this.http.put<IEtapeTest>(`${this.resourceUrl}/${this.getEtapeTestIdentifier(etapeTest)}`, etapeTest, { observe: 'response' });
  }

  partialUpdate(etapeTest: PartialUpdateEtapeTest): Observable<EntityResponseType> {
    return this.http.patch<IEtapeTest>(`${this.resourceUrl}/${this.getEtapeTestIdentifier(etapeTest)}`, etapeTest, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtapeTest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtapeTest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEtapeTestIdentifier(etapeTest: Pick<IEtapeTest, 'id'>): number {
    return etapeTest.id;
  }

  compareEtapeTest(o1: Pick<IEtapeTest, 'id'> | null, o2: Pick<IEtapeTest, 'id'> | null): boolean {
    return o1 && o2 ? this.getEtapeTestIdentifier(o1) === this.getEtapeTestIdentifier(o2) : o1 === o2;
  }

  addEtapeTestToCollectionIfMissing<Type extends Pick<IEtapeTest, 'id'>>(
    etapeTestCollection: Type[],
    ...etapeTestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const etapeTests: Type[] = etapeTestsToCheck.filter(isPresent);
    if (etapeTests.length > 0) {
      const etapeTestCollectionIdentifiers = etapeTestCollection.map(etapeTestItem => this.getEtapeTestIdentifier(etapeTestItem)!);
      const etapeTestsToAdd = etapeTests.filter(etapeTestItem => {
        const etapeTestIdentifier = this.getEtapeTestIdentifier(etapeTestItem);
        if (etapeTestCollectionIdentifiers.includes(etapeTestIdentifier)) {
          return false;
        }
        etapeTestCollectionIdentifiers.push(etapeTestIdentifier);
        return true;
      });
      return [...etapeTestsToAdd, ...etapeTestCollection];
    }
    return etapeTestCollection;
  }
}
