using System.Collections.Generic;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.Core.Boundary;
using Retrospector.Core.Crud;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;
using Retrospector.Core.Search;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;
using Retrospector.Crud;
using Retrospector.DataStorage;
using Retrospector.DataStorage.Factoids;
using Retrospector.DataStorage.Factoids.Interfaces;
using Retrospector.DataStorage.Medias;
using Retrospector.DataStorage.Medias.Interfaces;
using Retrospector.DataStorage.Reviews;
using Retrospector.DataStorage.Reviews.Interfaces;
using Retrospector.DataStorage.Search;
using Retrospector.DataStorage.Search.Interfaces;
using Retrospector.Search;

namespace Retrospector.Setup
{
    public class Startup
    {
        public DatabaseConfiguration DatabaseConfig { get; set; }

        public Startup(DatabaseConfiguration databaseConfig)
        {
            DatabaseConfig = databaseConfig;
        }

        public IServiceCollection ConfigureServices(IServiceCollection services)
        {
            return services
                .AddSingleton<DatabaseConfiguration>(DatabaseConfig)
                .AddTransient<ICrudDataGateway<Factoid>, FactoidDataGateway>()
                .AddTransient<ICrudDataGateway<Media>, MediaDataGateway>()
                .AddTransient<ICrudDataGateway<Review>, ReviewDataGateway>()
                .AddTransient<ICrudPresenter<Factoid>, CrudPresenter<Factoid>>()
                .AddTransient<ICrudPresenter<Media>, CrudPresenter<Media>>()
                .AddTransient<ICrudPresenter<Review>, CrudPresenter<Review>>()
                .AddTransient<IDatabaseContext, DatabaseContext>()
                .AddTransient<IFactoidMapper, FactoidMapper>()
                .AddTransient<IFactoidReducer, FactoidReducer>()
                .AddTransient<ILeafExpressionBuilder, LeafExpressionBuilder>()
                .AddTransient<ILeafOperator, LeafOperator>(p => BuildLeafOperator())
                .AddTransient<IMediaMapper, MediaMapper>()
                .AddTransient<IMediaReducer, MediaReducer>()
                .AddTransient<IMediaTypeMapper, MediaTypeMapper>()
                .AddTransient<IQueryBuilder, QueryBuilder>(p => BuildQueryBuilder(p.GetService<ILeafOperator>()))
                .AddTransient<IRequestRouter, RequestRouter>()
                .AddTransient<IReviewMapper, ReviewMapper>()
                .AddTransient<IReviewReducer, ReviewReducer>()
                .AddTransient<ISearchDataGateway, SearchDataGateway>()
                .AddTransient<ISearchFilterBuilder, SearchFilterBuilder>()
                .AddTransient<ISearchPresenter, SearchPresenter>()
                .AddTransient<IUseCase<CrudRequest<Factoid>>, CrudUseCase<Factoid>>()
                .AddTransient<IUseCase<CrudRequest<Media>>, CrudUseCase<Media>>()
                .AddTransient<IUseCase<CrudRequest<Review>>, CrudUseCase<Review>>()
                .AddTransient<IUseCase<SearchRequest>, SearchUseCase>();
        }

        private static LeafOperator BuildLeafOperator()
        {
            var commandDecoder = new Dictionary<string, RetrospectorAttribute>
            {
                {"T", RetrospectorAttribute.MediaTitle},
                {"C", RetrospectorAttribute.MediaCreator},
                {"S", RetrospectorAttribute.MediaSeason},
                {"E", RetrospectorAttribute.MediaEpisode},
                {"A", RetrospectorAttribute.MediaCategory},
                {"P", RetrospectorAttribute.MediaDescription},
                {"#", RetrospectorAttribute.ReviewRating},
                {"U", RetrospectorAttribute.ReviewUser},
                {"D", RetrospectorAttribute.ReviewDate},
                {"R", RetrospectorAttribute.ReviewContent},
                {"I", RetrospectorAttribute.FactoidTitle},
                {"O", RetrospectorAttribute.FactoidContent}
            };
            var comparatorDecoder = new Dictionary<string, Comparator>
            {
                {"=", Comparator.Equal},
                {">", Comparator.GreaterThan},
                {"<", Comparator.LessThan},
                {"~", Comparator.Contains},
            };
            return new LeafOperator("`", commandDecoder, comparatorDecoder);
        }

        private static QueryBuilder BuildQueryBuilder(ILeafOperator leafOperator)
        {
            return new (
                leafOperator,
                new BinaryOperator(OperatorType.And, ":"),
                new BinaryOperator(OperatorType.Or, "|"),
                new UnaryOperator(OperatorType.Not, "!")
            );
        }
    }
}