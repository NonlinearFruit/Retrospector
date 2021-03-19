using System;
using System.Collections.Generic;
using Microsoft.Extensions.DependencyInjection;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;
using Retrospector.DataStorage;
using Retrospector.DataStorage.Interfaces;
using Retrospector.DataStorage.Models;
using Retrospector.Main;
using Retrospector.Main.Interfaces;
using Retrospector.Search;
using Retrospector.Utilities;
using Retrospector.Utilities.Interfaces;

namespace Retrospector.Setup
{
    public class Startup
    {
        public Configuration Configuration { get; set; }

        public Startup(Configuration configuration)
        {
            Configuration = configuration;
        }

        public IServiceCollection ConfigureServices(IServiceCollection services)
        {
            return services
                .AddSingleton<Configuration>(Configuration)
                .AddTransient<IDatabaseContext, DatabaseContext>()
                .AddTransient<IFactoidReducer, FactoidReducer>()
                .AddTransient<ILeafExpressionBuilder, LeafExpressionBuilder>()
                .AddTransient<ILeafOperator, LeafOperator>(p => BuildLeafOperator())
                .AddTransient<ILogger, Logger>()
                .AddTransient<IMediaReducer, MediaReducer>()
                .AddTransient<IQueryBuilder, QueryBuilder>(p => BuildQueryBuilder(p.GetService<ILeafOperator>()))
                .AddTransient<IReviewReducer, ReviewReducer>()
                .AddTransient<ISearchDataGateway, SearchDataGateway>()
                .AddTransient<ISearchFilterBuilder, SearchFilterBuilder>()
                .AddTransient<IMainWindow, MainWindow>()
                .AddTransient<MainWindowViewModel>();
        }

        public void Configure(IServiceProvider provider)
        {
            provider.GetService<IDatabaseContext>().RunMigrations();
            provider.GetService<IMainWindow>().Show();
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